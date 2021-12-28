FROM node:17-alpine

WORKDIR /app
COPY . .

EXPOSE 8086

RUN npm ci --prod

CMD ["node", "src/app.js"]
