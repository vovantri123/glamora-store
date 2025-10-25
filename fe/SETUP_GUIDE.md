# 📋 Glamora Store Frontend - Setup Guide

## ✅ Đã cài đặt thành công

### 🎯 Tech Stack

- ✅ **Next.js 16** - Framework React với App Router
- ✅ **TypeScript** - Type safety
- ✅ **Tailwind CSS v4** - Styling với PostCSS
- ✅ **ESLint** - Code linting với Next.js config
- ✅ **Prettier** - Code formatting
- ✅ **Husky** - Git hooks
- ✅ **lint-staged** - Pre-commit formatting

### 📦 Packages được cài đặt

```json
{
  "dependencies": {
    "next": "16.0.0",
    "react": "19.2.0",
    "react-dom": "19.2.0"
  },
  "devDependencies": {
    "typescript": "^5",
    "@types/node": "^20",
    "@types/react": "^19",
    "@types/react-dom": "^19",
    "tailwindcss": "^4",
    "@tailwindcss/postcss": "^4",
    "eslint": "^9",
    "eslint-config-next": "16.0.0",
    "eslint-config-prettier": "^10.1.8",
    "eslint-plugin-prettier": "^5.5.4",
    "prettier": "^3.6.2",
    "prettier-plugin-tailwindcss": "^0.7.1",
    "husky": "^9.1.7",
    "lint-staged": "^16.2.6",
    "babel-plugin-react-compiler": "1.0.0"
  }
}
```

## 🚀 Cách sử dụng

### Development

```bash
# Chạy dev server
npm run dev

# Mở http://localhost:3000
```

### Build & Production

```bash
# Build production
npm run build

# Chạy production server
npm start
```

### Code Quality

```bash
# Kiểm tra linting
npm run lint

# Fix linting issues
npm run lint:fix

# Format toàn bộ code
npm run format

# Kiểm tra formatting
npm run format:check
```

## 🎨 Auto-formatting

### Mỗi lần commit:

1. **Husky** tự động chạy pre-commit hook
2. **lint-staged** chỉ xử lý files đã staged
3. **ESLint** tự động fix issues
4. **Prettier** format code
5. **Tailwind plugin** sắp xếp class names

### Ví dụ:

```bash
# Code trước khi commit (badly formatted)
const obj={foo:"bar",baz:123}
<div className="p-4 bg-blue-500 text-white">

# Sau khi commit (auto-formatted)
const obj = { foo: 'bar', baz: 123 };
<div className="rounded-lg bg-blue-500 p-4 text-white">
```

## 📁 Project Structure

```
fe/
├── .husky/                   # Git hooks
│   └── pre-commit           # Auto-format on commit
├── .vscode/                 # VSCode config
│   ├── settings.json       # Format on save, ESLint
│   └── extensions.json     # Recommended extensions
├── public/                  # Static files
├── src/
│   ├── app/                # Next.js App Router
│   │   ├── layout.tsx     # Root layout
│   │   ├── page.tsx       # Home page
│   │   └── globals.css    # Global styles
│   ├── components/         # React components
│   └── lib/               # Utilities
├── .editorconfig           # Editor consistency
├── .env.example           # Environment template
├── .env.local             # Local environment
├── .prettierrc            # Prettier config
├── .prettierignore        # Prettier ignore
├── eslint.config.mjs      # ESLint config
├── tsconfig.json          # TypeScript config
├── tailwind.config.ts     # Tailwind config (auto-generated)
├── postcss.config.mjs     # PostCSS config
├── next.config.ts         # Next.js config
└── package.json           # Dependencies & scripts
```

## 🔧 Configuration Files

### Prettier (`.prettierrc`)

- Semi: true (dùng dấu chấm phẩy)
- Single quotes: true (dùng single quotes)
- Print width: 100 characters
- Tab width: 2 spaces
- Trailing comma: ES5
- Auto-sort Tailwind classes

### ESLint (`eslint.config.mjs`)

- Next.js recommended rules
- TypeScript support
- Prettier integration
- Warning cho unused vars
- Warning cho explicit any

### TypeScript (`tsconfig.json`)

- Strict mode enabled
- Path alias: `@/*` → `src/*`
- Target: ES2017
- JSX: preserve (Next.js handles it)

### Tailwind CSS v4

- Auto PostCSS processing
- JIT mode enabled
- Prettier plugin auto-sorts classes

## 🛠️ VSCode Extensions (Recommended)

Extensions sẽ được suggest khi mở project:

1. **ESLint** - Linting
2. **Prettier** - Formatting
3. **Tailwind CSS IntelliSense** - Class autocomplete
4. **PostCSS Language Support** - PostCSS syntax

### Auto-setup trong VSCode:

- ✅ Format on save
- ✅ ESLint auto-fix on save
- ✅ Tailwind IntelliSense
- ✅ TypeScript path mapping

## 📝 Best Practices

### 1. Commit Messages

```bash
git commit -m "feat: add user authentication"
git commit -m "fix: resolve login error"
git commit -m "refactor: optimize product listing"
```

### 2. Component Structure

```tsx
// src/components/Button.tsx
interface ButtonProps {
  label: string;
  onClick: () => void;
}

export default function Button({ label, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="rounded-lg bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
    >
      {label}
    </button>
  );
}
```

### 3. API Calls

```tsx
// src/lib/api.ts
const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function fetchProducts() {
  const res = await fetch(`${API_URL}/products`);
  return res.json();
}
```

### 4. Environment Variables

```bash
# .env.local (not committed)
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## 🐛 Troubleshooting

### Husky hooks không chạy?

```bash
npm run prepare
git config core.hooksPath .husky
```

### ESLint errors?

```bash
npm run lint:fix
```

### Prettier không format?

```bash
npm run format
```

### TypeScript errors?

```bash
# Xóa cache và rebuild
rm -rf .next
npm run dev
```

## 🔗 Tích hợp với Backend

Backend đang chạy ở: `http://localhost:8080`

```tsx
// Example: Fetch từ Spring Boot API
const products = await fetch('http://localhost:8080/api/products').then((res) =>
  res.json()
);
```

## 📚 Resources

- [Next.js Docs](https://nextjs.org/docs)
- [Tailwind CSS v4](https://tailwindcss.com/docs)
- [TypeScript Handbook](https://www.typescriptlang.org/docs)
- [React 19 Docs](https://react.dev)

## ✨ Features

- ✅ React Server Components
- ✅ App Router với file-based routing
- ✅ TypeScript strict mode
- ✅ Tailwind CSS v4 với PostCSS
- ✅ ESLint + Prettier
- ✅ Git hooks auto-formatting
- ✅ VSCode integration
- ✅ Environment variables
- ✅ Optimized fonts (Geist)
- ✅ Image optimization
- ✅ Fast Refresh

## 🎯 Next Steps

1. ✅ Setup project structure - DONE
2. 📝 Create components library
3. 🔐 Add authentication
4. 🛒 Build product pages
5. 💳 Integrate payment
6. 🚀 Deploy to production

---

**Happy Coding! 🚀**
