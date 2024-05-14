const storageName = "userToken"


export function getStoredInfo(){
    const storedInfo = window.localStorage.getItem(storageName)
    return storedInfo ? JSON.parse(storedInfo) : null
}

export function setStoredInfo(userInfo){
    window.localStorage.setItem(storageName,JSON.stringify(userInfo))
}

export function deleteStoredInfo(){
    window.localStorage.removeItem(storageName)
}