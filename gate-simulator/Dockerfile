FROM node:12
WORKDIR /usr/src/gate-simulator
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 9999
CMD [ "node", "app.js" ]