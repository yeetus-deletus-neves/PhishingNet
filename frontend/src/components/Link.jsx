import { defaultFetch } from "../../../extension/src/utils/fetch";

export function LinkPage(){

    async function saveToCache(cacheName, key, value) {
        try {
            const data = new Response(JSON.stringify(value));
            
            caches.open(cacheName).then((cache) => {
                cache.put(key, data);
            });
            //await cache.put(key, data); 
            console.log(`Data saved to cache: ${value}`);
        } catch (error) {
            console.error('Error saving data to cache:', error);
        }
    };

    async function getFromCache(cacheName, key) {
        try {
            const cache = await caches.open(cacheName);
            const cachedResponse = await cache.match(key);
      
            if (cachedResponse) {
              const data = await cachedResponse.text();
              console.log(`Data retrieved from cache with key "${key}":`, data);
              return data;
            } else {
              console.log(`No data found in cache with key "${key}".`);
              return null;
            }
        } catch (error) {
            console.error('Error retrieving data from cache:', error);
            return null;
        }
      }

    const searchParams = new URLSearchParams(window.location.search)
    const azureCode = searchParams.get('code')
    /saveToCache("test", "code", azureCode);
    let t;
    (async () => {
        t = getFromCache("test", "code");
    })();
    console.log(this);
    
    /*if(azureCode){
        (async () => {
            const linkRsp = await defaultFetch(
                'http://localhost:8080/user',
                "POST",
                {
                    'Content-Type': 'application/json',
                    'Bearer Token': '<search in cache, or get through provider>',
                },
                {
                "token": azureCode
                }
            )
        })();
    }*/

    return (
    <div className="container">
        <div id="container" className="center">
            <h1 className="center">Link was successfull</h1>
            <h2>Azure code: {azureCode}</h2>
        </div>
    </div>);
}
