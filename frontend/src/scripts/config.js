const PRODUCT_BASE_URL = "http://localhost:8092";

function getProductApiUrl(endpoint) {
    return `${PRODUCT_BASE_URL}${endpoint}`;
}
