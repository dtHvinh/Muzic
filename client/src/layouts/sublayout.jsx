export default function SubLayout({ children }) {
  return (
    <div className="container">
      <div className="max-w-6xl mx-auto">{children}</div>
    </div>
  );
}
