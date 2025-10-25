# 🚀 Quick Start - Glamora Store Frontend

## 📦 Installation (Lần đầu)

```bash
cd fe
npm install
```

## 🔥 Development

```bash
npm run dev
```

Mở http://localhost:3000

## ⚡ Quick Commands

```bash
# Development
npm run dev          # Start dev server

# Build
npm run build        # Build production
npm start            # Run production

# Code Quality
npm run lint         # Check errors
npm run lint:fix     # Fix errors
npm run format       # Format code
```

## 🎯 What's Included?

- ✅ Next.js 16 + TypeScript
- ✅ Tailwind CSS v4
- ✅ ESLint + Prettier
- ✅ Auto-format on commit (Husky)
- ✅ Example components & utilities
- ✅ VSCode settings

## 📁 Key Files

```
src/
├── app/              # Pages (Next.js routes)
├── components/       # React components
│   └── Button.tsx   # Example button
└── lib/             # Utilities
    ├── api.ts       # API calls
    ├── constants.ts # Constants
    └── utils.ts     # Helpers
```

## 🎨 Example Usage

### 1. Create Component

```tsx
// src/components/ProductCard.tsx
interface ProductCardProps {
  name: string;
  price: number;
}

export default function ProductCard({ name, price }: ProductCardProps) {
  return (
    <div className="rounded-lg bg-white p-4 shadow">
      <h3>{name}</h3>
      <p>{formatCurrency(price)}</p>
    </div>
  );
}
```

### 2. Create Page

```tsx
// src/app/products/page.tsx
import { getProducts } from '@/lib/api';

export default async function ProductsPage() {
  const products = await getProducts();

  return (
    <div className="container mx-auto p-4">
      <h1>Products</h1>
      {/* Render products */}
    </div>
  );
}
```

### 3. API Call

```tsx
import { apiGet, apiPost } from '@/lib/api';

// GET
const products = await apiGet('/products');

// POST
const result = await apiPost('/products', {
  name: 'Product',
  price: 100000,
});
```

## 🔧 Environment

Create `.env.local`:

```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## 📝 Commit

Code tự động format khi commit:

```bash
git add .
git commit -m "feat: add product page"
# ✨ Auto-format runs!
```

## 🆘 Help

- **Setup guide**: `SETUP_GUIDE.md`
- **Commit rules**: `COMMIT_CONVENTION.md`
- **Full docs**: `README.md`

## 🔗 Links

- [Next.js Docs](https://nextjs.org/docs)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [TypeScript](https://typescriptlang.org/docs)

---

**Ready to code! 🚀**
