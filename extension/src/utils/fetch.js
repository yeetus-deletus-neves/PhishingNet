export async function defaultFetch(
    uri,
    method,
    headers,
    body
){

    const response = await fetch(uri, method == 'GET' ?{
        method: method,
        headers: headers,
        signal: AbortSignal.timeout(3000)
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

    return response
}