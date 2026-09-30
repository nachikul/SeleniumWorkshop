/* ShopLite - tiny local "Amazon.in-style" demo app used as the Selenium
   automation target for this workshop. Pure client-side, no backend:
   all "persistence" lives in localStorage/sessionStorage so the site
   works offline from a file:// URL and never flakes due to network calls. */

const USERS = [
  { username: "student", password: "Workshop@123" },
  { username: "admin", password: "Admin@123" }
];

const PRODUCTS = [
  { id: "p1", name: "Wireless Mouse", category: "Electronics", price: 599, emoji: "\u{1F5B1}️" },
  { id: "p2", name: "Bluetooth Headphones", category: "Electronics", price: 1999, emoji: "\u{1F3A7}" },
  { id: "p3", name: "Mechanical Keyboard", category: "Electronics", price: 3499, emoji: "\u{2328}️" },
  { id: "p4", name: "Selenium WebDriver Book", category: "Books", price: 799, emoji: "\u{1F4D8}" },
  { id: "p5", name: "TestNG Cookbook", category: "Books", price: 649, emoji: "\u{1F4D7}" },
  { id: "p6", name: "Coffee Mug", category: "Home", price: 299, emoji: "☕" },
  { id: "p7", name: "Laptop Stand", category: "Home", price: 1299, emoji: "\u{1F4BB}" },
  { id: "p8", name: "Desk Lamp", category: "Home", price: 899, emoji: "\u{1F4A1}" }
];

const MAX_LOGIN_ATTEMPTS = 3;

function findUser(username, password) {
  return USERS.find(u => u.username === username && u.password === password);
}

function getLoggedInUser() {
  return sessionStorage.getItem("shoplite_user");
}

function requireLogin() {
  if (!getLoggedInUser()) {
    window.location.href = "login.html";
  }
}

function logout() {
  sessionStorage.removeItem("shoplite_user");
  sessionStorage.removeItem("shoplite_cart");
  window.location.href = "login.html";
}

function getCart() {
  const raw = sessionStorage.getItem("shoplite_cart");
  return raw ? JSON.parse(raw) : {};
}

function saveCart(cart) {
  sessionStorage.setItem("shoplite_cart", JSON.stringify(cart));
}

function addToCart(productId) {
  const cart = getCart();
  cart[productId] = (cart[productId] || 0) + 1;
  saveCart(cart);
  return cartItemCount(cart);
}

function removeFromCart(productId) {
  const cart = getCart();
  delete cart[productId];
  saveCart(cart);
  return cartItemCount(cart);
}

function cartItemCount(cart) {
  cart = cart || getCart();
  return Object.values(cart).reduce((sum, qty) => sum + qty, 0);
}
