// src/types/filter.ts

// 排序方式类型：可选 "asc"（升序）或 "desc"（降序）
export type SortOrder = "asc" | "desc";

/**
 * 发送给后端的筛选请求参数类型。
 * 不包含任何函数，仅包含筛选条件本身。
 */
export interface FilterParams {
  categories: string[];
  prices: string[];
  meals: string[];
  sortOrder: SortOrder;
}

/**
 * Zustand 状态管理中使用的完整筛选状态。
 * 包含筛选条件 + 更新函数。
 */
export interface FilterState extends FilterParams {
  setCategories: (c: string[]) => void;
  setPrices: (p: string[]) => void;
  setMeals: (m: string[]) => void;
  setSortOrder: (v: SortOrder) => void;
  clear: () => void;
}
