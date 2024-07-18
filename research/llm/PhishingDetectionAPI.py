from fastapi import FastAPI, Form, HTTPException
from pydantic import BaseModel
from PhishingDetector import PhishingDetector
from langdetect import detect
from translate import Translator


app = FastAPI()
detector = PhishingDetector()
translator = Translator(from_lang='pt', to_lang='en')

class PromptRequest(BaseModel):
    prompt: str

@app.post("/prompt")
async def generate_response(prompt: str = Form(...)):
    
    text = prompt
    lang = detect(text)
    
    if lang != 'en' and lang != 'pt':
        raise HTTPException(status_code=400, detail="Message is not in a supported language!")
    
    if lang == 'pt':
        text = translator(text)
    
    response = detector.analyze(text)
    
    return response

# Used for test
@app.post("/promptest")
async def generate_response(prompt: PromptRequest):
    
    text = prompt.prompt
    lang = detect(text)
    
    if lang != 'en' and lang != 'pt':
        raise HTTPException(status_code=400, detail="Message is not in a supported language!")
    
    if lang == 'pt':
        text = translator(text)
    
    response = detector.analyze(text)
    
    return response