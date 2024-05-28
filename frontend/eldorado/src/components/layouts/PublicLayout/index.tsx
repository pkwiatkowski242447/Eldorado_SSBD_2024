import {ReactNode} from "react";
import Container from "react-bootstrap/Container";
import {Toaster} from "@/components/ui/toaster.tsx";
import SiteHeader from "@/components/SiteHeader.tsx";

interface LayoutProps {
    children: ReactNode,
    hideHeader?: boolean
}

//hideHeader is a boolean prop that is optional and defaults to false if not provided
//this is done so that the header can be visible on desired pages
function PublicLayout({children, hideHeader = true}: LayoutProps) {
    return (
        <div>
            {!hideHeader && <SiteHeader/>}
            <Container>{children}</Container>
            <Toaster/>
        </div>
    )
}

export default PublicLayout;