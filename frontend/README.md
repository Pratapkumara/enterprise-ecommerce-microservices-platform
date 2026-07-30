# Pratap Store Frontend

React and Vite storefront for the Enterprise E-Commerce Microservices Platform.

## Local development

```bash
npm install
npm run dev
```

The Vite development server proxies `/api` requests to the API Gateway on port 8082.

## Production image

```bash
docker build -t ecommerce-frontend:1.0 .
docker run --rm -p 8081:80 ecommerce-frontend:1.0
```

The production NGINX container serves the SPA, exposes `/health`, and proxies `/api` to the Kubernetes `api-gateway` service.
