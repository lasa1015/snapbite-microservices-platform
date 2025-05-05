import { create } from "zustand";
import { FilterContextType, SortOrder } from "../types/filter";

export const useFilterStore = create<FilterContextType>((set) => ({
  categories: [],
  prices: [],
  meals: [],
  sortOrder: "desc",

  setCategories: (c) => set({ categories: c }),
  setPrices: (p) => set({ prices: p }),
  setMeals: (m) => set({ meals: m }),
  setSortOrder: (v: SortOrder) => set({ sortOrder: v }),

  clear: () =>
    set({
      categories: [],
      prices: [],
      meals: [],
      sortOrder: "desc",
    }),
}));
