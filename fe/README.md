# Glamora Store - Frontend

Modern e-commerce frontend built with Next.js 16, TypeScript, and Tailwind CSS.

## 🚀 Tech Stack

- **Framework:** Next.js 16 (App Router)
- **Language:** TypeScript
- **Styling:** Tailwind CSS v4
- **Code Quality:**
  - ESLint (Next.js config)
  - Prettier (with Tailwind plugin)
  - Husky (Git hooks)
  - lint-staged (Pre-commit formatting)

## 📦 Getting Started

### Prerequisites

- Node.js 20+ and npm

### Installation

```bash
# Install dependencies
npm install

# Run development server
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to view the app.

## 🛠️ Available Scripts

| Script                 | Description               |
| ---------------------- | ------------------------- |
| `npm run dev`          | Start development server  |
| `npm run build`        | Build for production      |
| `npm start`            | Start production server   |
| `npm run lint`         | Run ESLint                |
| `npm run lint:fix`     | Fix ESLint errors         |
| `npm run format`       | Format code with Prettier |
| `npm run format:check` | Check code formatting     |

## 📝 Code Quality

### Auto-formatting on Commit

This project uses Husky and lint-staged to automatically format code before each commit:

- **ESLint** fixes JavaScript/TypeScript issues
- **Prettier** formats all files (including Tailwind class sorting)
- Runs only on staged files for better performance

### Manual Formatting

```bash
# Format all files
npm run format

# Check formatting without changes
npm run format:check

# Fix linting issues
npm run lint:fix
```

## 📁 Project Structure

```
fe/
├── src/
│   ├── app/              # Next.js App Router pages
│   │   ├── layout.tsx    # Root layout
│   │   └── page.tsx      # Home page
│   ├── components/       # React components (create as needed)
│   ├── lib/             # Utility functions (create as needed)
│   └── styles/          # Global styles
├── public/              # Static assets
├── .husky/             # Git hooks
├── .vscode/            # VSCode settings
└── ...config files
```

## 🎨 Code Style

- **Indentation:** 2 spaces
- **Quotes:** Single quotes for JS/TS, double for JSX
- **Semicolons:** Required
- **Line width:** 100 characters
- **Trailing commas:** ES5 style
- **Tailwind classes:** Auto-sorted by Prettier

## 🔧 VSCode Setup

Recommended extensions (auto-suggested when you open the project):

- ESLint
- Prettier - Code formatter
- Tailwind CSS IntelliSense
- PostCSS Language Support

Settings are pre-configured in `.vscode/settings.json` for:

- Format on save
- ESLint auto-fix on save
- Tailwind CSS IntelliSense for classNames

## 📚 Learn More

- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS v4](https://tailwindcss.com/docs)
- [TypeScript Handbook](https://www.typescriptlang.org/docs)

## 🤝 Contributing

1. Create a feature branch from `main`
2. Make your changes
3. Commit (auto-formatting will run via Husky)
4. Push and create a Pull Request

Pre-commit hooks will automatically:

- Run ESLint and fix issues
- Format code with Prettier
- Sort Tailwind classes

## 📄 License

Private project - All rights reserved
