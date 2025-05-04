import { Outlet } from "react-router-dom";
import CartSidebar from "../components/CartSidebar";

export default function MainLayout() {
  return (
    <div style={{ display: "flex" }}>
      <div style={{ flex: 1 }}>
        <Outlet />
      </div>
      <CartSidebar />
    </div>
  );
}
