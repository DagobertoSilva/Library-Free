# Build stage
FROM node:18-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps || true

COPY . .

FROM nginx:alpine

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/pages /usr/share/nginx/html/pages
COPY --from=builder /app/css /usr/share/nginx/html/css
COPY --from=builder /app/javascript /usr/share/nginx/html/javascript
COPY --from=builder /app/img /usr/share/nginx/html/img

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
