import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useState} from "react";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {CreateSectorType, sectorType} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type createParkingFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
    parkingId: string
}

function CreateParkingForm({setDialogOpen, refresh, parkingId}:createParkingFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [newSector, setNewSector] = useState<CreateSectorType>({name:"", type:sectorType.UNCOVERED, maxPlaces:0, weight:1 })
    const formSchema = z.object({
        name: z.string().min(5, {message:t("create.sector.form.sector.name.too.short")})
            .max(5, {message: t("create.sector.form.sector.name.too.long")}).regex(RegExp("^[A-Z]{2}-[0-9]{2}$"), {message: t("create.sector.form.sector.name.regex.not.met")}),
        maxPlaces: z.coerce.number({message: t("create.sector.form.sector.max.places.has.to.be.number")}).int({message: t("create.sector.form.sector.max.places.has.to.be.integer")}).min(0, {message: t("create.sector.form.sector.max.places.at.least.zero")}).max(1000,{message: t("create.sector.form.sector.max.places.at.most.thousand")}),
        type: z.enum(["UNCOVERED", "COVERED", "UNDERGROUND"]),
        weight: z.coerce.number({message: t("create.sector.form.sector.weight.has.to.be.number")}).int({message: t("create.sector.form.sector.weight.has.to.be.integer")}).min(1, {message: t("create.sector.form.sector.weight.too.low")}).max(100,{message: t("create.sector.form.sector.weight.too.high")})
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: "",
            maxPlaces: 0,
            type: "UNCOVERED",
            weight: 1
        },
    })

    async function handleAlertDialog(){
        api.createSector(parkingId, newSector)
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
            setNewSector({
                name: values.name,
                type: sectorType[values.type],
                maxPlaces: values.maxPlaces,
                weight: values.weight
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
                <FormField
                    control={form.control}
                    name="name"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.sector.form.name")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="AA-01" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                control={form.control}
                name="maxPlaces"
                render={({field}) => (
                    <FormItem>
                        <div className="grid gap-4">
                            <FormLabel className="text-left">{t("create.sector.form.max.places")}</FormLabel>
                            <FormControl>
                                <Input placeholder="0" {...field} />
                            </FormControl>
                            <FormMessage/>
                        </div>
                    </FormItem>
                )}
                />
                <FormField
                    control={form.control}
                    name="weight"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.sector.form.weight")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="0" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="type"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.sector.form.sector.type")}</FormLabel>
                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select a sector type." />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value="UNCOVERED">{t("create.sector.form.sector.type.uncovered")}</SelectItem>
                                        <SelectItem value="COVERED">{t("create.sector.form.sector.type.covered")}</SelectItem>
                                        <SelectItem value="UNDERGROUND">{t("create.sector.form.sector.type.underground")}</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </FormItem>
                    )}
                />
                <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("create.sector.form.create")
                    )}
                </Button>
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("create.sector.form.sector.are.you.sure.you.want.to.create.new.sector")}
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleAlertDialog}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default CreateParkingForm
