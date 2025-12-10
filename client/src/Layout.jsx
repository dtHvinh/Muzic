import Sidebar from "./components/sidebar";

export default function Layout() {
  return (
    <div className="flex h-screen flex-col bg-background">
      <div className="flex flex-1 overflow-hidden">
        <Sidebar />
      </div>
    </div>
  );
}
