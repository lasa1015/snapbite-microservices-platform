// src/components/ConfirmDialog.tsx
import React from "react";

type Props = {
  open: boolean;
  title: string;
  description: string;
  onConfirm: () => void;
  onCancel: () => void;
};

export default function ConfirmDialog({
  open,
  title,
  description,
  onConfirm,
  onCancel,
}: Props) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-[1001]">



      {/* 对话框 */}
      <div className="relative bg-white p-6 rounded-xl shadow-[0px_0px_8px_rgba(0,0,0,0.3)] w-[360px] font-outfit text-center">
        <h2 className="text-xl font-semibold mb-2">{title}</h2>
        <p className="text-m text-gray-600 mb-6">{description}</p>
        <div className="flex justify-center gap-4">
          <button
            onClick={onCancel}
            className="px-4 py-2 rounded-md border border-gray-300 hover:bg-gray-100 transition"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-primary text-white rounded-md hover:bg-red-700 transition"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  );
}
