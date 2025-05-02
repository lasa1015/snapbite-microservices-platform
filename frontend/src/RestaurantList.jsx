import React, { useEffect, useState } from 'react';

const RestaurantList = () => {
  const [restaurants, setRestaurants] = useState([]);

  useEffect(() => {
    fetch('/api/restaurants') // 向后端发请求
      .then((res) => res.json())
      .then((data) => setRestaurants(data))
      .catch((err) => console.error('Failed to fetch:', err));
  }, []);

  return (
    <div style={{ padding: '1rem' }}>
      <h2>🍽️ Restaurant List</h2>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {restaurants.map((r, idx) => (
          <li key={idx} style={{ marginBottom: '1rem', borderBottom: '1px solid #ccc', paddingBottom: '0.5rem' }}>
            <strong>{r.name || '(No name)'}</strong> <br />
            📍 {r.displayAddress} <br />
            ☎️ {r.displayPhone} <br />
            ⭐ {r.rating} ({r.reviewCount} reviews)
          </li>
        ))}
      </ul>
    </div>
  );
};

export default RestaurantList;
