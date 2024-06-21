import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb";
import {Slash} from "lucide-react";
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {api} from "@/api/api";
import {CreateReservationForm} from "@/components/forms/CreateReservationForm.tsx";
import handleApiError from "@/components/HandleApiError.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useEffect, useState} from "react";
import {SectorInfoType} from "@/types/Parking.ts";
import {Button} from "@/components/ui/button.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {Pathnames} from "@/router/pathnames.ts";

function CreateReservationInSectorPage() {
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const [sectorInfo, setSectorInfo] = useState<SectorInfoType>();
    const navigate = useNavigate();

    const handleReservationSubmit = async (beginTime: Date, endTime: Date) => {
        if (id) {
            await api.createReservation(id, beginTime.toISOString(), endTime.toISOString()).then(() => {
                toast({
                    title: t("reservation.created.title"),
                    description: t("reservation.created.message"),
                });
            }).catch((error) => {
                handleApiError(error);
            });
        }
    };

    const fetchSectorInfo = (sectorId: string) => {
        if (sectorId) {
            api.getSectorInfo(sectorId)
                .then(response => {
                    setSectorInfo(response.data);
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    useEffect(() => {
        if (id) {
            fetchSectorInfo(id);
        }
    }, []);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className="pl-2">
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.parkingList)}>{t("breadcrumb.active.parking.list")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.create.reservation")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("create.reservation.in.sector.page.create.reservation")}</h1>
                <div>
                    <Table className="p-10 flex-grow mt-2">
                        <TableHeader>
                            <TableRow className={"text-center p-10"}>
                                <TableHead
                                    className="text-center">{t("createReservationPage.name")}</TableHead>
                                <TableHead
                                    className="text-center">{t("createReservationPage.type")}</TableHead>
                                <TableHead
                                    className="text-center">{t("createReservationPage.maxPlaces")}</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody className={"text-center"}>
                            <TableRow key={sectorInfo?.id} className="flex-auto">
                                <TableCell>{sectorInfo?.name}</TableCell>
                                {/* @ts-expect-error what? */}
                                <TableCell><Badge variant={"default"}>{t(sectorInfo?.type)}</Badge></TableCell>
                                <TableCell>{sectorInfo?.maxPlaces}</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </div>
                <div className={"pt-5 flex justify-center"}>
                    <CreateReservationForm onSubmit={handleReservationSubmit}/>
                </div>
            </div>
        </div>
    );
}
export default CreateReservationInSectorPage;