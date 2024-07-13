FROM python

WORKDIR /usr/app
RUN pip install --upgrade pip setuptools
RUN pip install transformers
RUN pip install fastapi
RUN pip install "numpy<2.0.0"
RUN pip install accelerate
RUN pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu121
COPY ./research/llm /usr/app
CMD ["fastapi", "dev", "./PhishingDetectionAPI.py", "--host", "0.0.0.0", "--port", "8000"]