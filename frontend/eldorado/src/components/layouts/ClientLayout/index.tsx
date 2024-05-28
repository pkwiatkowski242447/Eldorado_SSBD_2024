import {ReactNode} from "react";
import Container from "react-bootstrap/Container";
import {Toaster} from "@/components/ui/toaster.tsx";

interface LayoutProps {
    children: ReactNode
}

function ClientLayout({children}:LayoutProps){
    return (
        <div>
            <Container>{children}</Container>
            <Toaster/>
        </div>
    )
}

export default ClientLayout;