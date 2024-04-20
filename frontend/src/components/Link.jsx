import { defaultFetch } from "../../../extension/src/utils/fetch";

export async function LinkPage(){

    const saveToCache = async (cacheName, key, value) => {
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


    const searchParams = new URLSearchParams(window.location.search)
    const azureCode = searchParams.get('code')
    saveToCache("tsest", "code", azureCode);

    if(azureCode){
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
    }

    return (
    <div className="container">
        <div id="container" className="center">
            <h1 className="center">Link was successfull</h1>
            <h2>Azure code: {azureCode}</h2>
        </div>
    </div>);
}
