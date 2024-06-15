const storageName = "userToken"
const mapName = "contentMap"

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

export function getStoredContent(conversationID){
    const storedMap = getStoredMap()
    return storedMap ? storedMap.find((e)=> e.key == conversationID) : null
}

export function getStoredMap(){
    const storedInfo = window.localStorage.getItem(mapName)
    return storedInfo ? JSON.parse(storedInfo) : null
}

export function setStoredMap(conversationID,content){
    let storedMap = getStoredMap()
    if(!storedMap){
        storedMap = []
    }
    storedMap.push({key:conversationID,value:content})
    if(storedMap.length >100){
        storedMap.shift()
    }

    window.localStorage.setItem(mapName,JSON.stringify(storedMap))
}

export function deleteStoredMap(){
    window.localStorage.removeItem(mapName)
}
