import SiteHeader from "@/components/SiteHeader.tsx";
import {Breadcrumb, BreadcrumbItem, BreadcrumbLink, BreadcrumbList,} from "@/components/ui/breadcrumb.tsx";

import {useTranslation} from "react-i18next";

function HomePage() {
    const {t} = useTranslation();

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <Breadcrumb className={"pt-5 pl-2"}>
                <BreadcrumbList>
                    <BreadcrumbItem>
                        <BreadcrumbLink>{t("breadcrumb.home")}</BreadcrumbLink>
                    </BreadcrumbItem>
                </BreadcrumbList>
            </Breadcrumb>
        </div>
    )
}

export default HomePage