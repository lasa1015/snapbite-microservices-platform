// src/stores/filterStore.ts

import { create } from "zustand";
import { FilterState, SortOrder } from "../types/filter";

/**
 * useFilterStore 是用于管理餐厅筛选条件的全局状态。
 * 它包括筛选的分类（categories）、价格（prices）、用餐时间（meals）和排序方式（sortOrder）。
 * 同时提供了更新这些筛选条件的方法，适用于整个前端应用。
 */



export const useFilterStore = create<FilterState>((set) => ({
  
  // 当前选中的餐厅分类，如 ["Pizza", "Chinese"]
  categories: [],

  // 当前选中的价格区间，如 ["€", "€€"]
  prices: [],

  // 当前选中的用餐时间，如 ["breakfast", "dinner"]
  meals: [],

  // 当前选中的排序方式，默认为 "desc"
  sortOrder: "desc",

  // 设置分类
  setCategories: (c) => set({ categories: c }),

  // 设置价格
  setPrices: (p) => set({ prices: p }),

  // 设置用餐时间
  setMeals: (m) => set({ meals: m }),

  // 设置排序方式
  setSortOrder: (v: SortOrder) => set({ sortOrder: v }),

  // 清空所有筛选条件，恢复为初始状态
  clear: () =>
    set({
      categories: [],
      prices: [],
      meals: [],
      sortOrder: "desc",
    }),
}));
