// src/components/RestaurantMap.tsx
interface Props {
  lat: number;
  lng: number;
}

export default function RestaurantMap({ lat, lng }: Props) {
  return (
    <div className="w-full h-[500px] rounded-lg overflow-hidden">

      <iframe
        width="100%"
        height="100%"
        loading="lazy"
        allowFullScreen
        src={`https://maps.google.com/maps?q=${lat},${lng}&z=15&output=embed`}
      />
    </div>
  );
}
