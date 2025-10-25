export default function TestComponent() {
  const badlyFormatted = { foo: 'bar', baz: 123 };
  console.log(badlyFormatted);

  return (
    <div className="rounded-lg bg-blue-500 p-4 text-white shadow-md">
      <h1>Test Component</h1>
      <p>This file has bad formatting</p>
    </div>
  );
}
