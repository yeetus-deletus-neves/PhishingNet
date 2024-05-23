export async function defaultFetch(
    uri,
    method,
    headers,
    body
){
    const response = await fetch(uri, method == 'GET' ?{
        method: method,
        headers: headers,
    } :{
        method: method,
        headers: headers,
        body: JSON.stringify(body)
    } )
    if (!response.ok) {
        const rsp = await response.json()
        throw {
            status: rsp.status,
            message: rsp.message,
            details: rsp.details
        }
    }
    return await response.json()
}