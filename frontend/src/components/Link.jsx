
export function LinkPage(){

    const searchParams = new URLSearchParams(window.location.search)
    const azureCode = searchParams.get('code')

    return (
    <div className="container">
        <div id="container" className="center">
            <h1 className="center">Link was successfull</h1>
            <h2>Azure code: {azureCode}</h2>
        </div>
    </div>);
}
