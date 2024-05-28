import SiteHeader from "@/components/SiteHeader.tsx";
import {Breadcrumb, BreadcrumbItem, BreadcrumbLink, BreadcrumbList,} from "@/components/ui/breadcrumb.tsx";

import {useTranslation} from "react-i18next";
import {Button} from "@/components/ui/button.tsx";

function HomePage() {
    const {t} = useTranslation();

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
        </div>
    )
}

export default HomePage