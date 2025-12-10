"use client";

import { Heart, Home, Library, Music2, PlusCircle, Search } from "lucide-react";
import { useState } from "react";
import { cn } from "../lib/utils";

const menuItems = [
  { icon: Home, label: "Home", active: true },
  { icon: Search, label: "Search" },
  { icon: Library, label: "Your Library" },
];

const playlists = Array.from({ length: 20 }, (_, i) => `Playlist ${i + 1}`);

export default function Sidebar() {
  const [activeItem, setActiveItem] = useState("Home");

  return (
    <aside className="hidden w-64 shrink-0 flex-col bg-sidebar p-6 md:flex">
      <div className="mb-8 flex items-center gap-2">
        <Music2 className="h-8 w-8 text-primary" />
        <span className="text-xl font-bold text-sidebar-foreground">Muzic</span>
      </div>

      <nav className="mb-8 space-y-2">
        {menuItems.map((item) => (
          <SidebarMenuItem
            key={item.label}
            icon={item.icon}
            label={item.label}
            active={activeItem === item.label}
            onClick={() => setActiveItem(item.label)}
          />
        ))}
      </nav>

      <div className="mb-4 space-y-2">
        <button className="flex w-full items-center gap-4 rounded-lg px-3 py-2.5 text-sm font-medium text-sidebar-foreground/70 transition-colors hover:bg-sidebar-accent/50 hover:text-sidebar-foreground">
          <PlusCircle className="h-5 w-5" />
          Create Playlist
        </button>
        <button className="flex w-full items-center gap-4 rounded-lg px-3 py-2.5 text-sm font-medium text-sidebar-foreground/70 transition-colors hover:bg-sidebar-accent/50 hover:text-sidebar-foreground">
          <Heart className="h-5 w-5" />
          Liked Songs
        </button>
      </div>

      <div className="my-4 h-px bg-sidebar-border" />

      <div className="flex-1 overflow-y-auto">
        <div className="space-y-1">
          <div className="pb-2 font-semibold text-sidebar-foreground">
            Your playlists
          </div>
          {playlists.map((playlist) => (
            <button
              key={playlist}
              className="block w-full truncate rounded px-3 py-2 text-left text-sm text-sidebar-foreground/60 transition-colors hover:text-sidebar-foreground"
            >
              {playlist}
            </button>
          ))}
        </div>
      </div>
    </aside>
  );
}

// eslint-disable-next-line no-unused-vars
const SidebarMenuItem = ({ icon: Icon, label, active, onClick }) => {
  return (
    <button
      key={label}
      onClick={onClick}
      className={cn(
        "flex w-full items-center gap-4 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
        active
          ? "bg-sidebar-accent text-primary"
          : "text-sidebar-foreground/70 hover:bg-sidebar-accent/50 hover:text-sidebar-foreground"
      )}
    >
      <Icon className="h-5 w-5" />
      {label}
    </button>
  );
};
