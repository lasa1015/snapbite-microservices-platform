// context/FilterContext.tsx
import { createContext, useContext, useState, ReactNode } from "react";

type FilterState = {
  categories: string[];
  prices: string[];
  meals: string[];
  sortOrder: "asc" | "desc";
  setCategories: (c: string[]) => void;
  setPrices: (p: string[]) => void;
  setMeals: (m: string[]) => void;
  setSortOrder: (v: "asc" | "desc") => void;
  clear: () => void;
};

const FilterContext = createContext<FilterState | null>(null);

export function FilterProvider({ children }: { children: ReactNode }) {
  const [categories, setCategories] = useState<string[]>([]);
  const [prices, setPrices] = useState<string[]>([]);
  const [meals, setMeals] = useState<string[]>([]);
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("desc");

  const clear = () => {
    setCategories([]);
    setPrices([]);
    setMeals([]);
    setSortOrder("desc");
  };

  return (
    <FilterContext.Provider value={{
      categories, prices, meals, sortOrder,
      setCategories, setPrices, setMeals, setSortOrder,
      clear
    }}>
      {children}
    </FilterContext.Provider>
  );
}

export function useFilter() {
  const ctx = useContext(FilterContext);
  if (!ctx) throw new Error("useFilter must be used within a FilterProvider");
  return ctx;
}
