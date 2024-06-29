export function AboutPage(){

    return(
        <div>
            <div className="card" style={{ textAlign:"center"}}>
                <div className="card-body container-fluid">
                    <div className="row justify-content-center" style={{alignContent:'center'}}>
                        <div className="col-md-1" style={{textAlign: "left"}}>
                            <img className="d-inline-block align-top" width="100" height="100"src="./icons/pj.png" alt="Card image cap"/>
                        </div>
                        <div className="col-md-6" style={{textAlign: "center", alignContent:'center'}}>
                            <h3 className="card-text"> The phishing net application was made with the help and feedback from the judicial police's cybercrime department UNC3T </h3>
                        </div>
                    </div>
                </div>
            </div>
            <h2>Developers:</h2>
            <div className="row card-deck" style={{ textAlign:"center"}}>
                
                <div className="card column" style= {{width: "10rem"}}>
                    <div className="card-body">
                        <h5 className="card-title">Manuel Henriques</h5>
                        <p className="card-text"><img height="20px" src="./icons/email_logo.png"/> : a47202@alunos.isel.pt</p>
                        <a href="https://github.com/manuelchenriques" className="btn btn-dark"><img height="20rem" src="./icons/github-mark-white.svg"/></a>
                    </div>
                </div>
                <div className="card column" style= {{width: "10rem"}}>
                    <div className="card-body">
                        <h5 className="card-title">Miguel Neves</h5>
                        <p className="card-text"><img height="20px" src="./icons/email_logo.png"/> : a47230@alunos.isel.pt</p>
                        <a href="https://github.com/yeetus-deletus-neves" className="btn btn-dark"><img height="20rem" src="./icons/github-mark-white.svg"/></a>
                    </div>
                </div>
                <div className="card column" style= {{width: "10rem"}}>
                    <div className="card-body">
                        <h5 className="card-title">Tiago Pardal</h5>
                        <p className="card-text"><img height="20px" src="./icons/email_logo.png"/> : a47206@alunos.isel.pt</p>
                        <a href="https://github.com/tp323" className="btn btn-dark"><img height="20rem" src="./icons/github-mark-white.svg"/></a>
                    </div>
                </div>
            </div>
            <h2>Helpfull contacts and websites:</h2>
            <div className="row card-deck" style={{ textAlign:"center"}}>
                <div className="card" style={{ textAlign:"center"}}>
                    <div className="card-body">
                        <h5 className="card-title">UNC3T</h5>
                        <p className="card-text">Localition: Novo edifício-sede da Polícia Judiciária, Rua Gomes Freire 1169-007 Lisboa</p>
                        <p className="card-text">Telephone: 211 967 000</p>
                        <p className="card-text">Eletronic complaint: <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                                    window.open('https://qe.pj.pt','_blank')}}>https://qe.pj.pt</a>
                        </p>
                    </div>
                </div>
                <div className="card" style={{ textAlign:"center"}}>
                    <div className="card-body">
                        <h5 className="card-title">APAV - Odivelas</h5>
                        <p className="card-text">Localition: APAV - Gabinete de Apoio à Vítima de Odivelas, Parque do Pingo Doce, R. Júlio Borba, 2620-101 Póvoa de Santo Adrião</p>
                        <p className="card-text">Telephone: 800 502 357</p>
                        <p className="card-text">Website: <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                                    window.open('https://apav.pt/cibercrime/','_blank')}}>https://apav.pt/cibercrime/</a>
                        </p>
                    </div>
                </div>
                <div className="card" style={{ textAlign:"center"}}>
                    <div className="card-body">
                        <h5 className="card-title">Gabinete Cibercrime - Ministério Público</h5>
                        <p className="card-text">Localition: Rua do Vale de Pereiro, n.º 2 - 2.º, 1269-113 Lisboa-Portugal</p>
                        <p className="card-text">Telephone: 213 921 900</p>
                        <p className="card-text">E-mail: cibercrime@pgr.pt</p>
                        <p className="card-text">Website: <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                                    window.open('http://cibercrime.ministeriopublico.pt/','_blank')}}>http://cibercrime.ministeriopublico.pt/</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    )
}