import {ReactNode} from "react";
import Container from "react-bootstrap/Container";
import {Toaster} from "@/components/ui/toaster.tsx";
import SiteHeader from "@/components/SiteHeader.tsx";

interface LayoutProps {
    children: ReactNode
}

function ClientLayout({children}:LayoutProps){
    return (
        <div>
            <SiteHeader/>
            <Container>{children}</Container>
            <Toaster/>
        </div>
    )
}

export default ClientLayout;