FROM nginx:1.13.8

COPY nginx.conf /etc/nginx/conf.d/

RUN rm -rf /usr/share/nginx/html/*

COPY build/ /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
