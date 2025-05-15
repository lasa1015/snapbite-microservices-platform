
package com.shaluo.userservice.service;

import com.shaluo.userservice.dto.request.RegisterRequest;
import com.shaluo.userservice.dto.response.UserResponse;
import com.shaluo.userservice.model.postgres.Role;
import com.shaluo.userservice.model.postgres.User;
import com.shaluo.userservice.repository.postgres.UserRepository;
import com.shaluo.userservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {

        // 如果使用 @InjectMocks，通常不需要手动 setField，但如需注入 mock 密集对象，可加
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(userService, "jwtUtil", jwtUtil);
    }

    // 当 用户名和邮箱都没被占用 时，能否正确注册用户。
    @Test
    void register_shouldSaveUser_whenUsernameAndEmailNotExist() {

        // 构造一个注册请求，模拟用户输入的注册信息
        RegisterRequest request = new RegisterRequest();
        request.setUsername("alice");
        request.setPassword("password123");
        request.setEmail("alice@example.com");
        request.setRole("USER");

        // 模拟数据库中：用户名不存在
        //  when(...).thenReturn(...) 是 Mockito 的“行为模拟语法”
        // “当 userRepository 的 existsByUsername 方法被调用，且参数为 alice 时，就返回 false。”
        // 你不能在单元测试中连接真实数据库、真实加密服务、真实网络请求。所以你需要 Mock
        // 在这个测试中，userRepository.existsByUsername("alice") 实际上并没有访问数据库，而是被 Mockito 接管了。Mockito 拿到了这个调用，用我们提前定义好的 thenReturn(false) 假装给了它一个数据库返回值。”
        when(userRepository.existsByUsername("alice")).thenReturn(false);

        //  模拟数据库中：邮箱也不存在
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);

        // 模拟密码加密的结果（例如加密后变成了 encoded123）
        when(passwordEncoder.encode("password123")).thenReturn("encoded123");

        // 模拟保存用户时，直接返回保存的那个 user 对象（不做其他处理）
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // 【 验证标准1 】
        // 执行注册逻辑 userService.register(request)，不应该抛出任何异常
        // 来自 JUnit 5，是一个断言语法
        // 断言（Assert）执行 userService.register(request) 时，不应该抛出任何异常。
        assertDoesNotThrow(() -> userService.register(request));

        // 【 验证标准2 】
        // 验证 save() 方法是否被调用，且传进去的 User 各字段都正确
        User expectedUser = new User();
        expectedUser.setUsername("alice");
        expectedUser.setEmail("alice@example.com");
        expectedUser.setPassword("encoded123");
        expectedUser.setRole(Role.USER);

        verify(userRepository).save(refEq(expectedUser));

    }


    // 当用户名存在且密码正确时，是否成功生成 JWT token
    @Test
    void login_shouldReturnToken_whenPasswordCorrect() {

        // 创建一个模拟的 User 对象（模拟数据库中查到的用户）
        User user = new User();
        user.setUsername("bob");
        user.setPassword("encoded123"); // 模拟数据库中保存的是加密后的密码
        user.setRole(Role.USER);

        // 模拟数据库查询：当调用 findByUsername("bob") 时，返回上面的 user
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        // 模拟密码校验逻辑：用户输入的 raw123 加密后刚好匹配数据库的 encoded123
        when(passwordEncoder.matches("raw123", "encoded123")).thenReturn(true);

        // 模拟 token 生成逻辑：用户登录成功后，生成一个假的 token
        when(jwtUtil.generateToken("bob", "USER")).thenReturn("mocked-token");

        // 执行登录逻辑
        // 【 userService.login(...) 是真的。 mock 的 userRepository、passwordEncoder、jwtUtil 是假的】
        String token = userService.login("bob", "raw123");

        // 断言返回的 token 应该等于我们预期的 "mocked-token"
        assertEquals("mocked-token", token);
    }


    // 如果密码不对，系统是否能正确抛出异常
    @Test
    void login_shouldThrowException_whenPasswordIncorrect() {

        // 创建一个模拟用户，假设这是数据库里查到的用户
        User user = new User();
        user.setUsername("bob");
        user.setPassword("encoded123"); // 模拟数据库中存储的是加密后的密码

        // 模拟查询用户：用户名为 bob 时，能查到这个用户（模拟数据库返回）
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        // 模拟密码验证失败：用户输入的是 wrong，而数据库存的是 encoded123，加密后不匹配
        when(passwordEncoder.matches("wrong", "encoded123")).thenReturn(false);

        // 调用登录逻辑，预期会抛出 RuntimeException（因为密码验证失败）
        Exception ex = assertThrows(RuntimeException.class, () -> userService.login("bob", "wrong"));

        // 断言异常消息是否正确（确保抛出的不是其他错误，而是“密码错误”）
        assertEquals("密码错误", ex.getMessage());
    }



    // 用户存在时，是否能正确转换为 UserResponse 并返回
    @Test
    void getUserProfile_shouldReturnResponse_whenUserExists() {

        // 创建一个模拟的用户对象（模拟数据库中的记录）
        User user = new User();
        user.setId(1L);
        user.setUsername("bob");
        user.setEmail("bob@example.com");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        // 模拟 userRepository 查找用户名时返回这个用户
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        // 调用实际的 getUserProfile 方法
        UserResponse response = userService.getUserProfile("bob");

        // 断言返回结果中各字段是否与用户对象匹配（DTO 映射是否正确）
        assertEquals("bob", response.getUsername());
        assertEquals("bob@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    void register_shouldThrowException_whenUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("alice");
        request.setEmail("alice@example.com");
        request.setPassword("pass");
        request.setRole("USER");

        when(userRepository.existsByUsername("alice")).thenReturn(true);

        Exception ex = assertThrows(RuntimeException.class, () -> userService.register(request));
        assertEquals("用户名已存在", ex.getMessage());
    }


    @Test
    void register_shouldThrowException_whenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("alice");
        request.setEmail("alice@example.com");
        request.setPassword("pass");
        request.setRole("USER");

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        Exception ex = assertThrows(RuntimeException.class, () -> userService.register(request));
        assertEquals("邮箱已存在", ex.getMessage());
    }



}
