// src/types/filter.ts

export type SortOrder = "asc" | "desc";

export type FilterOptions = {
  categories: string[];
  prices: string[];
  meals: string[];
  sortOrder: SortOrder;
};

export type FilterContextType = FilterOptions & {
  setCategories: (c: string[]) => void;
  setPrices: (p: string[]) => void;
  setMeals: (m: string[]) => void;
  setSortOrder: (v: SortOrder) => void;
  clear: () => void;
};
