export async function saveToCache(cacheName, key, value) {
    try {
        const data = new Response(JSON.stringify(value));
        
        caches.open(cacheName).then((cache) => {
            cache.put(key, data);
        });
    } catch (error) {
        console.error('Error saving data to cache:', error);
    }
};

export async function getFromCache(cacheName, key) {
    try {
        const cache = await caches.open(cacheName);
        const cachedResponse = await cache.match(key);
  
        if (cachedResponse) {
          const data = await cachedResponse.text();
          return data;
        } else {
          return null;
        }
    } catch (error) {
        console.error('Error retrieving data from cache:', error);
        return null;
    }
}