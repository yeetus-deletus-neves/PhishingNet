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
        throw {
            status: response.status,
            message: response.statusText
        }
    }
    console.log(response)
    return await response.json()
}