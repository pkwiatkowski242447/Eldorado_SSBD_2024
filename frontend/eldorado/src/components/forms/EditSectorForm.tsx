import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription, AlertDialogFooter,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {EditSectorType, sectorType} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type editSectorFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
    sectorId: string
    sectorDeactivate: Date
}

function EditSectorForm({setDialogOpen, refresh, sectorId, sectorDeactivate}:editSectorFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [editedSector, setEditedSector] = useState<EditSectorType>({id:"", parkingId:"", version:"", name:"", type:sectorType.UNCOVERED, maxPlaces:0, weight:0, signature:""})
    const [sector, setSector] = useState<EditSectorType>({id:"", parkingId:"", version:"", name:"", type:sectorType.UNCOVERED, maxPlaces:0, weight:0, signature:""});
    const formSchema = z.object({
        maxPlaces: z.coerce.number({message: t("edit.sector.form.sector.max.places.has.to.be.number")}).int({message: t("edit.sector.form.sector.max.places.has.to.be.integer")}).min(0, {message: t("edit.sector.form.sector.max.places.at.least.zero")}).max(1000,{message: t("edit.sector.form.sector.max.places.at.most.thousand")}),
        type: z.enum(["UNCOVERED", "COVERED", "UNDERGROUND"]),
        weight: z.coerce.number({message: t("edit.sector.form.sector.weight.has.to.be.number")}).int({message: t("edit.sector.form.sector.weight.has.to.be.integer")}).min(1, {message: t("edit.sector.form.sector.weight.too.low")}).max(100,{message: t("edit.sector.form.sector.weight.too.high")})
    })

    useEffect(() => {
        api.getSectorById(sectorId)
            .then((response) => {
                setSector({...response.data, signature: response.headers['etag']})
            })
            .catch(error => {
                handleApiError(error);
            });
    }, []);


    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            maxPlaces: sector.maxPlaces,
            type: sector.type,
            weight: sector.weight
        },
    })

    useEffect(() => {
        console.log(sectorDeactivate);
        console.log(new Date());
        form.reset({
            maxPlaces: sector.maxPlaces,
            type: sector.type,
            weight: sector.weight
        });
    }, [sector]);

    async function handleAlertDialog(){
        api.modifySector(editedSector)
            .then(() => {
                refresh();
                setAlertDialogOpen(false);
                setDialogOpen(false)})
            .catch(error => {
                setAlertDialogOpen(false);
                handleApiError(error);
            });
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            setEditedSector({
                id: sector.id,
                parkingId: sector.parkingId,
                version: sector.version,
                name: sector.name,
                type: sectorType[values.type],
                maxPlaces: values.maxPlaces,
                weight: values.weight,
                signature: sector.signature
            })
            setAlertDialogOpen(true);
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                {sectorDeactivate < (new Date()) && <FormField
                    control={form.control}
                    name="maxPlaces"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.sector.form.max.places")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="1" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />}
                <FormField
                    control={form.control}
                    name="weight"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.sector.form.weight")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="1" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                {sectorDeactivate < (new Date()) && <FormField
                    control={form.control}
                    name="type"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.sector.form.sector.type")}</FormLabel>
                                <Select onValueChange={field.onChange} value={field.value}>
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder={t("create.sector.form.select.sector.type")} />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value="UNCOVERED">{t("edit.sector.form.sector.type.uncovered")}</SelectItem>
                                        <SelectItem value="COVERED">{t("edit.sector.form.sector.type.covered")}</SelectItem>
                                        <SelectItem value="UNDERGROUND">{t("edit.sector.form.sector.type.underground")}</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </FormItem>
                    )}
                />}
                <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("edit.sector.form.edit")
                    )}
                </Button>
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("edit.sector.form.are.you.sure.you.want.to.edit.this.sector")}
                    </AlertDialogDescription>
                    <AlertDialogFooter>
                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                        <AlertDialogAction onClick={handleAlertDialog}>
                            {t("general.ok")}
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default EditSectorForm
