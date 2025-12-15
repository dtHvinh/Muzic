import SearchBar from "./components/searchbar";
import Sidebar from "./components/sidebar";

export default function Layout({ children }) {
  return (
    <div className={"flex h-screen flex-col bg-background"}>
      <div className="flex flex-1 overflow-hidden">
        <Sidebar />
        <main className="flex-1 overflow-y-auto bg-background">
          <SearchBar />

          <div className="space-y-8 px-6 pb-32 flex justify-center">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
}
