FROM node
WORKDIR /usr/app
COPY ./frontend/package*.json ./
RUN npm install
COPY ./frontend ./
CMD ["npm", "run", "dev"]