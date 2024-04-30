import { defaultFetch } from "../../../extension/src/utils/fetch";
import { getFromCache } from "../scripts/cache";

export function LinkPage(){

    const searchParams = new URLSearchParams(window.location.search)
    const azureCode = searchParams.get('code')

    return (
    <div className="container">
        <div id="container" className="center">
            <h1 className="center">Link was successfull</h1>
            <h2>Azure code: {azureCode}</h2>
            <button type="button" onClick={ async ()=>{
                if(azureCode){
                    const t = await getFromCache("test","userToken")
                    const linkRsp = await defaultFetch(
                        'http://localhost:8080/user/link',
                        "POST",
                        {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${t}`
                        },
                        {
                            "token": azureCode
                        }
                    )
                }
            }}>Link</button>
        </div>
    </div>);
}
