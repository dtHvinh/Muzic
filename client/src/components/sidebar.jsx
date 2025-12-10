"use client";

import { Heart, Home, ListMusic, Music, Music2, Search } from "lucide-react";
import { useState } from "react";
import Link from '../components/core/link';
import { cn } from "../lib/utils";

const menuItems = [
  { icon: Home, href: 'home', label: "Home", active: true },
  { icon: Search, href: 'search', label: "Search" },
];

const menuItems2 = [
  { icon: ListMusic, href: 'playlist', label: "Playlist" },
  { icon: Music, href: 'artist', label: "Artists" },
  { icon: Heart, href: 'liked', label: "Liked Songs" },
];

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
            href={item.href}
            active={activeItem === item.label}
            onClick={() => setActiveItem(item.label)}
          />
        ))}
      </nav>

      <div className="mb-4 space-y-2">
        <nav className="mb-8 space-y-2">
          {menuItems2.map((item) => (
            <SidebarMenuItem
              key={item.label}
              icon={item.icon}
              label={item.label}
              href={item.href}
              active={activeItem === item.label}
              onClick={() => setActiveItem(item.label)}
            />
          ))}
        </nav>
      </div>

    </aside>
  );
}

// eslint-disable-next-line no-unused-vars
const SidebarMenuItem = ({ icon: Icon, label, active, onClick, href }) => {
  return (
    <Link
      href={href}
      onClick={onClick}
      className={cn(
        "flex w-full items-center gap-4 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
        active
          ? "bg-sidebar-accent text-primary"
          : "hover:bg-sidebar-accent/50 hover:text-sidebar-foreground"
      )}
    >
      <Icon className="h-5 w-5" />
      {label}
    </Link>
  );
};
