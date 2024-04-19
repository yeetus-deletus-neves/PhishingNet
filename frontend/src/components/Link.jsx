import { defaultFetch } from "../../../extension/src/utils/fetch";

export async function LinkPage(){

    const searchParams = new URLSearchParams(window.location.search)
    const azureCode = searchParams.get('code')
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
