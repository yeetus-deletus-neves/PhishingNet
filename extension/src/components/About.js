

export function About(){

    return(
        <div>
            <button type="button" class="btn btn-dark" onClick={()=>{
                window.open('http://localhost:3000/','_blank')
            }}>
                Phishing Net <span class="badge badge-light">?</span>
            </button> 
        </div>
    )
}