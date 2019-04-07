FROM node:10-alpine AS builder

COPY package.json package-lock.json ./

RUN npm ci && mkdir /ng-app && mv ./node_modules ./ng-app

WORKDIR /ng-app

COPY . .

RUN $(npm bin)/ng build --prod --output-path=dist

FROM nginx:alpine

VOLUME /etc/keystore

COPY ./nginx-default.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /ng-app/dist /usr/share/nginx/html

EXPOSE 443
