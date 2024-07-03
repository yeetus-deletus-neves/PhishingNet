from fastapi import FastAPI
from pydantic import BaseModel
from PhishingDetector import PhishingDetector


app = FastAPI()
detector = PhishingDetector()

class PromptRequest(BaseModel):
    prompt: str

@app.post("/prompt")
async def generate_response(prompt_request: PromptRequest):
    response = detector.analyze(prompt_request.prompt)
    
    return response