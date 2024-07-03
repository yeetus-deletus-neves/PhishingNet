import os
import torch
from transformers import AutoModelForSequenceClassification, AutoTokenizer
import torch.nn.functional as funct
import json

class PhishingDetector:
    
    model_id = "ealvaradob/bert-finetuned-phishing"
    model_path = "./model/bert-finetuned-phishing"
    my_token = "[HUGGINGFACES_API_KEY]"

    def __init__(self) -> None:
        
        ## Selects the GPU if possible. This is possible through CUDA.
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        print(f"Using device: {self.device}")
        self.__prepare_model()
    
    ## Loads the model into memory. If the folder containing the model is empty, dowloads it through huggingfaces
    def __prepare_model(self) -> None:
        
        ## Checks if the path where the model should be is there and is not empty. In case either of these checks fail, downloads the model.
        if not os.path.exists(self.model_path) or len(os.listdir(self.model_path)) == 0:
            print("Model not found.\n\nDownloading model...\n\n")
            self.tokenizer = AutoTokenizer.from_pretrained(self.model_id, token=self.my_token)
            self.model = AutoModelForSequenceClassification.from_pretrained(
                self.model_id,
                token=self.my_token,
                torch_dtype=torch.float16,  ## Using float16 for better performance
                low_cpu_mem_usage=True
            ).to(self.device)
            
            ## Loads the model into memory
            os.makedirs(self.model_path, exist_ok=True)
            self.tokenizer.save_pretrained(self.model_path)
            self.model.save_pretrained(self.model_path, max_shard_size="2GB")
            print("Download complete!")
        else:
        ## Model was found and is loaded into memory
            print("Model found!")
            self.tokenizer = AutoTokenizer.from_pretrained(self.model_path)
            self.model = AutoModelForSequenceClassification.from_pretrained(
                self.model_path,
                torch_dtype=torch.float16,  # Using float16 for better performance
                low_cpu_mem_usage=True
            ).to(self.device)

    ## Analyzes input string and returns the probability of it being phishing
    def analyze(self, input_string) -> list:
        ## Checks if input is a String
        if not isinstance(input_string, str):
            raise ValueError("Input must be a string!")
        
        inputs = self.tokenizer(input_string, return_tensors="pt").to(self.device)

        ## Disables grandients to increase performance and calculates the result
        with torch.no_grad():  
            logits = self.model(**inputs).logits
            probabilities = funct.softmax(logits, dim=-1)

        scores = probabilities[0].cpu().numpy()
        labels = self.model.config.id2label

        ## Selects the results related to the phishing probability score
        target_index = 0 if labels[0] == 'phishing' else 1
        processed_score = round(float(scores[target_index]) * 100, 2)

        return {
            labels[target_index]: processed_score
        }
