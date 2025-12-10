import { MessageCircleWarning } from 'lucide-react'

export default function Fallback() {
    return (
        <div className="border-red-200 rounded-2xl border-2 h-52 p-8 absolute top-32 left-1/2">
            <div className='flex items-center justify-between space-x-4'>
                <span className="text-3xl">Page not found</span>
                <MessageCircleWarning />
            </div>
        </div>
    )
}