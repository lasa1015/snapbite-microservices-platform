import { createContext, useContext, useState, ReactNode } from "react";

type CartContextType = {
  reloadFlag: number;
  triggerReload: () => void;
};

const CartContext = createContext<CartContextType | undefined>(undefined);

export function CartProvider({ children }: { children: ReactNode }) {
  const [reloadFlag, setReloadFlag] = useState(0);

  const triggerReload = () => {
    setReloadFlag((prev) => prev + 1);
  };

  return (
    <CartContext.Provider value={{ reloadFlag, triggerReload }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error("useCart must be used within a CartProvider");
  }
  return context;
}
