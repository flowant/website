FROM node:10 AS builder

WORKDIR /app

COPY . .

RUN npm install && \
    npm run build

FROM nginx:alpine

VOLUME /etc/keystore

COPY ./nginx-default.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist/* /usr/share/nginx/html/

EXPOSE 443
