"use client";

import { useMemo, useState } from "react";

type Product = {
  id: number;
  name: string;
  category: string;
  price: number;
  oldPrice: number;
  rating: number;
  reviews: number;
  icon: string;
  tone: string;
  badge?: string;
};

const products: Product[] = [
  { id: 1, name: "Enterprise Laptop Pro", category: "Electronics", price: 74999, oldPrice: 89999, rating: 4.8, reviews: 284, icon: "💻", tone: "blue", badge: "Bestseller" },
  { id: 2, name: "Nova 5G Smartphone", category: "Mobiles", price: 32999, oldPrice: 39999, rating: 4.7, reviews: 618, icon: "📱", tone: "violet", badge: "New" },
  { id: 3, name: "Studio Wireless Headphones", category: "Electronics", price: 4999, oldPrice: 7999, rating: 4.6, reviews: 423, icon: "🎧", tone: "slate", badge: "38% off" },
  { id: 4, name: "Active Smart Watch", category: "Electronics", price: 6999, oldPrice: 9999, rating: 4.5, reviews: 197, icon: "⌚", tone: "orange" },
  { id: 5, name: "AirFlow Mixer Grinder", category: "Appliances", price: 3499, oldPrice: 4999, rating: 4.4, reviews: 156, icon: "⚙️", tone: "green" },
  { id: 6, name: "Urban Everyday Backpack", category: "Fashion", price: 1499, oldPrice: 2499, rating: 4.6, reviews: 347, icon: "🎒", tone: "yellow" },
  { id: 7, name: "PureGlow Skin Essentials", category: "Beauty", price: 999, oldPrice: 1499, rating: 4.7, reviews: 236, icon: "✨", tone: "rose" },
  { id: 8, name: "Smart LED Desk Lamp", category: "Home", price: 1799, oldPrice: 2499, rating: 4.5, reviews: 129, icon: "💡", tone: "cyan" },
];

const categories = ["All", "Electronics", "Mobiles", "Fashion", "Home", "Appliances", "Beauty"];

const money = (value: number) =>
  new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR", maximumFractionDigits: 0 }).format(value);

export default function Home() {
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("All");
  const [cart, setCart] = useState<number[]>([]);
  const [wishlist, setWishlist] = useState<number[]>([]);
  const [cartOpen, setCartOpen] = useState(false);
  const [accountOpen, setAccountOpen] = useState(false);

  const visibleProducts = useMemo(() => {
    const normalized = query.trim().toLowerCase();
    return products.filter((product) => {
      const categoryMatch = category === "All" || product.category === category;
      const queryMatch =
        !normalized ||
        product.name.toLowerCase().includes(normalized) ||
        product.category.toLowerCase().includes(normalized);
      return categoryMatch && queryMatch;
    });
  }, [query, category]);

  const cartProducts = cart.map((id) => products.find((product) => product.id === id)).filter(Boolean) as Product[];
  const subtotal = cartProducts.reduce((sum, product) => sum + product.price, 0);

  const addToCart = (id: number) => {
    setCart((current) => [...current, id]);
    setCartOpen(true);
  };

  const removeFromCart = (index: number) => {
    setCart((current) => current.filter((_, itemIndex) => itemIndex !== index));
  };

  const toggleWishlist = (id: number) => {
    setWishlist((current) => current.includes(id) ? current.filter((item) => item !== id) : [...current, id]);
  };

  const scrollToProducts = () => {
    document.getElementById("products")?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <main>
      <div className="announcement">
        <p>Free delivery on orders above ₹499</p>
        <div>
          <span>Track Order</span>
          <span>Help Centre</span>
        </div>
      </div>

      <header className="site-header">
        <a className="brand" href="#" aria-label="Pratap Store home">
          <span className="brand-mark" aria-hidden="true">P</span>
          <span>Pratap Store</span>
        </a>

        <label className="search-box">
          <span aria-hidden="true">⌕</span>
          <input
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Search products, brands and more"
            aria-label="Search products"
          />
          {query && <button onClick={() => setQuery("")} aria-label="Clear search">×</button>}
        </label>

        <div className="header-actions">
          <button className="icon-action" onClick={() => setAccountOpen(true)}>
            <span aria-hidden="true">◯</span><b>Account</b>
          </button>
          <button className="icon-action desktop-action" onClick={() => setCategory("All")}>
            <span aria-hidden="true">♡</span><b>Wishlist</b>
            {wishlist.length > 0 && <em>{wishlist.length}</em>}
          </button>
          <button className="icon-action" onClick={() => setCartOpen(true)}>
            <span aria-hidden="true">🛒</span><b>Cart</b>
            {cart.length > 0 && <em>{cart.length}</em>}
          </button>
        </div>
      </header>

      <nav className="category-nav" aria-label="Product categories">
        {categories.slice(1).map((item) => (
          <button key={item} className={category === item ? "active" : ""} onClick={() => { setCategory(item); scrollToProducts(); }}>
            {item}
          </button>
        ))}
        <button onClick={() => { setCategory("All"); scrollToProducts(); }}>Grocery</button>
      </nav>

      <section className="hero">
        <div className="hero-copy">
          <p className="eyebrow">New arrival · Business series</p>
          <h1>Power your work.<br />Anywhere.</h1>
          <p className="hero-description">
            High-performance business laptop built for speed, focus and all-day productivity.
          </p>
          <div className="hero-actions">
            <button className="primary-button" onClick={() => addToCart(1)}>Shop now</button>
            <button className="text-button" onClick={scrollToProducts}>Explore laptops <span>→</span></button>
          </div>
        </div>
        <div className="hero-visual" aria-label="Enterprise Laptop Pro">
          <div className="blue-shape shape-one" />
          <div className="blue-shape shape-two" />
          {/* Vinext serves this generated local asset directly. */}
          {/* eslint-disable-next-line @next/next/no-img-element */}
          <img src="/hero-laptop.png" alt="Graphite high-performance business laptop" />
          <div className="price-pill"><small>From</small><strong>₹74,999</strong></div>
        </div>
      </section>

      <section className="trust-strip" aria-label="Shopping benefits">
        <div><span>▣</span><p><strong>Free delivery</strong><small>Orders above ₹499</small></p></div>
        <div><span>↩</span><p><strong>7-day returns</strong><small>Easy and reliable</small></p></div>
        <div><span>♢</span><p><strong>Secure payments</strong><small>Protected checkout</small></p></div>
        <div><span>✓</span><p><strong>Genuine products</strong><small>Quality guaranteed</small></p></div>
      </section>

      <section className="products-section" id="products">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Curated for you</p>
            <h2>{query ? `Results for “${query}”` : category === "All" ? "Trending deals" : category}</h2>
          </div>
          <div className="filter-pills" aria-label="Filter products">
            {categories.slice(0, 5).map((item) => (
              <button key={item} className={category === item ? "active" : ""} onClick={() => setCategory(item)}>{item}</button>
            ))}
          </div>
        </div>

        {visibleProducts.length > 0 ? (
          <div className="product-grid">
            {visibleProducts.map((product) => {
              const saved = wishlist.includes(product.id);
              const discount = Math.round((1 - product.price / product.oldPrice) * 100);
              return (
                <article className="product-card" key={product.id}>
                  <div className={`product-image ${product.tone}`}>
                    {product.badge && <span className="product-badge">{product.badge}</span>}
                    <button className={`wishlist-button ${saved ? "saved" : ""}`} onClick={() => toggleWishlist(product.id)} aria-label={`${saved ? "Remove" : "Add"} ${product.name} ${saved ? "from" : "to"} wishlist`}>
                      {saved ? "♥" : "♡"}
                    </button>
                    <span className="product-icon" aria-hidden="true">{product.icon}</span>
                  </div>
                  <div className="product-info">
                    <p className="product-category">{product.category}</p>
                    <h3>{product.name}</h3>
                    <div className="rating"><strong>★ {product.rating}</strong><span>({product.reviews})</span></div>
                    <div className="product-price">
                      <strong>{money(product.price)}</strong>
                      <del>{money(product.oldPrice)}</del>
                      <span>{discount}% off</span>
                    </div>
                    <button className="add-button" onClick={() => addToCart(product.id)}>Add to cart</button>
                  </div>
                </article>
              );
            })}
          </div>
        ) : (
          <div className="empty-state">
            <span>⌕</span>
            <h3>No products found</h3>
            <p>Try a different search or browse all categories.</p>
            <button className="primary-button" onClick={() => { setQuery(""); setCategory("All"); }}>View all products</button>
          </div>
        )}
      </section>

      <section className="promo-row">
        <div className="promo-card blue-promo">
          <p>Upgrade your workspace</p>
          <h2>Smart tech for better work.</h2>
          <button onClick={() => { setCategory("Electronics"); scrollToProducts(); }}>Shop electronics →</button>
        </div>
        <div className="promo-card pale-promo">
          <p>Member advantage</p>
          <h2>Extra savings, every week.</h2>
          <button onClick={() => setAccountOpen(true)}>Join Pratap Plus →</button>
        </div>
      </section>

      <section className="newsletter">
        <div>
          <p className="eyebrow">Stay in the loop</p>
          <h2>Good deals, delivered thoughtfully.</h2>
          <p>Weekly offers and useful product updates. No noise.</p>
        </div>
        <form onSubmit={(event) => event.preventDefault()}>
          <input type="email" placeholder="Your email address" aria-label="Email address" required />
          <button className="primary-button">Subscribe</button>
        </form>
      </section>

      <footer>
        <a className="brand footer-brand" href="#"><span className="brand-mark">P</span><span>Pratap Store</span></a>
        <p>Smart shopping, dependable service.</p>
        <div><a href="#">About</a><a href="#">Support</a><a href="#">Returns</a><a href="#">Privacy</a></div>
        <small>© 2026 Pratap Store. Built with a production-ready microservices platform.</small>
      </footer>

      {cartOpen && (
        <div className="overlay" onMouseDown={() => setCartOpen(false)}>
          <aside className="cart-drawer" onMouseDown={(event) => event.stopPropagation()} aria-label="Shopping cart">
            <div className="drawer-heading"><div><p className="eyebrow">Your selection</p><h2>Shopping cart</h2></div><button onClick={() => setCartOpen(false)} aria-label="Close cart">×</button></div>
            {cartProducts.length === 0 ? (
              <div className="drawer-empty"><span>🛒</span><h3>Your cart is empty</h3><p>Great products are only a click away.</p><button className="primary-button" onClick={() => { setCartOpen(false); scrollToProducts(); }}>Start shopping</button></div>
            ) : (
              <>
                <div className="cart-items">
                  {cartProducts.map((product, index) => (
                    <div className="cart-item" key={`${product.id}-${index}`}>
                      <span className={`mini-product ${product.tone}`}>{product.icon}</span>
                      <div><h3>{product.name}</h3><p>{money(product.price)}</p></div>
                      <button onClick={() => removeFromCart(index)} aria-label={`Remove ${product.name}`}>×</button>
                    </div>
                  ))}
                </div>
                <div className="cart-total"><span>Subtotal</span><strong>{money(subtotal)}</strong></div>
                <p className="shipping-note">Free delivery included. Taxes calculated at checkout.</p>
                <button className="checkout-button">Secure checkout</button>
              </>
            )}
          </aside>
        </div>
      )}

      {accountOpen && (
        <div className="overlay centered" onMouseDown={() => setAccountOpen(false)}>
          <section className="account-modal" onMouseDown={(event) => event.stopPropagation()}>
            <button className="modal-close" onClick={() => setAccountOpen(false)} aria-label="Close">×</button>
            <span className="modal-mark">P</span>
            <p className="eyebrow">Welcome back</p>
            <h2>Sign in to Pratap Store</h2>
            <p>Access your orders, wishlist and personalised deals.</p>
            <form onSubmit={(event) => event.preventDefault()}>
              <label>Email address<input type="email" placeholder="you@example.com" required /></label>
              <label>Password<input type="password" placeholder="••••••••" required /></label>
              <button className="primary-button">Sign in</button>
            </form>
            <p className="modal-foot">New here? <button>Create an account</button></p>
          </section>
        </div>
      )}
    </main>
  );
}
