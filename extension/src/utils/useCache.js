import {
    useState,
    useEffect,
} from 'react'

export function useCache(cacheName,prop){

    const [loading, setLoading] = useState(false)
    const [content, setContent] = useState(undefined)

    useEffect(()=> {
        let cancelled = false
        async function doGetCache(){
            const token = await getFromCache(cacheName,prop)
            
            if (!cancelled) {
                setLoading(false)
                setContent(token)
            }
        }
        setLoading(true)
        doGetCache()
        return () => {
            cancelled = true
        }
    }, [cacheName,prop,setContent])
    
    return [content, loading]
}